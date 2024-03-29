package no.ntnu.ihb.vico.proxyfmu

import com.opensimulationplatform.proxyfmu.thrift.BootService
import com.opensimulationplatform.proxyfmu.thrift.FmuService
import com.opensimulationplatform.proxyfmu.thrift.Status
import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.FmuState
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import no.ntnu.ihb.fmi4j.util.OsUtil
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TFramedTransport
import org.apache.thrift.transport.TSocket
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class ProxySlave(
    remoteInfo: RemoteInfo,
    fmuFile: File,
    override val instanceName: String,
    override val modelDescription: CoSimulationModelDescription
) : SlaveInstance {

    private val client: FmuService.Client
    private val protocol: TBinaryProtocol

    override var isTerminated: Boolean = false
    override val lastStatus: FmiStatus
        get() = FmiStatus.OK

    private var process: Process? = null

    init {

        val host = remoteInfo.host
        val port = if (remoteInfo.hasPort) {
            val transport = TFramedTransport.Factory().getTransport(TSocket(host, remoteInfo.port!!))
            val protocol = TBinaryProtocol(transport)
            transport.open()
            val client = BootService.Client(protocol)
            val buf = ByteBuffer.wrap(fmuFile.readBytes())
            client.loadFromBinaryData(fmuFile.nameWithoutExtension, instanceName, buf).also {
                transport.close()
            }
        } else {

            var proxyFileName = "proxyfmu"
            if (OsUtil.isWindows) {
                proxyFileName += ".exe"
            }

            val proxyFile = File(proxyFileName)
            if (!proxyFile.exists()) {
                val proxy = ProxySlave::class.java.classLoader.getResourceAsStream(proxyFileName)!!
                FileOutputStream(proxyFile).use { fos ->
                    proxy.use {
                        it.copyTo(fos)
                    }
                }
            }

            val cmd = arrayOf(
                proxyFile.absolutePath,
                "--fmu", fmuFile.absolutePath,
                "--instanceName", instanceName
            )
            val pb = ProcessBuilder().apply {
                command(*cmd)
            }
            process = pb.start()

            val p = ArrayBlockingQueue<Int>(1)
            thread(true) {
                val br = process!!.inputStream.bufferedReader()
                while (true) {
                    val read = br.readLine() ?: break

                    if (read.startsWith("[proxyfmu] port=")) {
                        p.add(read.substring(16).toInt())
                    } else {
                        println(read)
                    }

                }
            }

            p.take()
        }

        val transport = TFramedTransport.Factory().getTransport(TSocket(host, port))
        protocol = TBinaryProtocol(transport)
        transport.open()
        client = FmuService.Client(protocol)
        client.instantiate()
    }

    override fun setupExperiment(start: Double, stop: Double, tolerance: Double): Boolean {
        val status = client.setupExperiment(start, stop, tolerance)
        return status == Status.OK_STATUS
    }

    override fun enterInitializationMode(): Boolean {
        val status = client.enterInitializationMode()
        return status == Status.OK_STATUS
    }

    override fun exitInitializationMode(): Boolean {
        val status = client.exitInitializationMode()
        return status == Status.OK_STATUS
    }

    override fun terminate(): Boolean {
        return if (!isTerminated) {
            val status = client.terminate()
            isTerminated = true
            status == Status.OK_STATUS
        } else {
            true
        }
    }

    override fun doStep(currentTime: Double, stepSize: Double): Boolean {
        val status = client.step(currentTime, stepSize)
        return status == Status.OK_STATUS
    }

    override fun readBoolean(vr: ValueReferences, ref: BooleanArray): FmiStatus {
        require(vr.size == ref.size)
        val read = client.readBoolean(vr.toList())
        for (i in read.value.indices) {
            ref[i] = read.value[i]
        }
        return if (read.status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun readInteger(vr: ValueReferences, ref: IntArray): FmiStatus {
        require(vr.size == ref.size)
        val read = client.readInteger(vr.toList())
        for (i in read.value.indices) {
            ref[i] = read.value[i]
        }
        return if (read.status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun readReal(vr: ValueReferences, ref: RealArray): FmiStatus {
        require(vr.size == ref.size)
        val read = client.readReal(vr.toList())
        for (i in read.value.indices) {
            ref[i] = read.value[i]
        }
        return if (read.status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun readString(vr: ValueReferences, ref: StringArray): FmiStatus {
        require(vr.size == ref.size)
        val read = client.readString(vr.toList())
        for (i in read.value.indices) {
            ref[i] = read.value[i]
        }
        return if (read.status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun writeBoolean(vr: ValueReferences, value: BooleanArray): FmiStatus {
        require(vr.size == value.size)
        val status = client.writeBoolean(vr.toList(), value.toList())
        return if (status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun writeInteger(vr: ValueReferences, value: IntArray): FmiStatus {
        require(vr.size == value.size)
        val status = client.writeInteger(vr.toList(), value.toList())
        return if (status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun writeReal(vr: ValueReferences, value: RealArray): FmiStatus {
        require(vr.size == value.size)
        val status = client.writeReal(vr.toList(), value.toList())
        return if (status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun writeString(vr: ValueReferences, value: StringArray): FmiStatus {
        require(vr.size == value.size)
        val status = client.writeString(vr.toList(), value.toList())
        return if (status == Status.OK_STATUS) {
            FmiStatus.OK
        } else {
            FmiStatus.Error
        }
    }

    override fun deSerializeFMUstate(state: ByteArray): FmuState {
        TODO("Not yet implemented")
    }

    override fun freeFMUstate(state: FmuState): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDirectionalDerivative(
        vUnknownRef: ValueReferences,
        vKnownRef: ValueReferences,
        dvKnown: RealArray
    ): RealArray {
        TODO("Not yet implemented")
    }

    override fun getFMUstate(): FmuState {
        TODO("Not yet implemented")
    }

    override fun reset(): Boolean {
        TODO("Not yet implemented")
    }

    override fun serializeFMUstate(state: FmuState): ByteArray {
        TODO("Not yet implemented")
    }

    override fun setFMUstate(state: FmuState): Boolean {
        TODO("Not yet implemented")
    }

    override fun close() {
        client.freeInstance()
        process?.waitFor(1000, TimeUnit.MILLISECONDS)
    }

}
