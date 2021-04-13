package no.ntnu.ihb.vico.proxyfmu

import com.opensimulationplatform.thrift.FmuService
import com.opensimulationplatform.thrift.Status
import no.ntnu.ihb.fmi4j.FmiStatus
import no.ntnu.ihb.fmi4j.FmuState
import no.ntnu.ihb.fmi4j.SlaveInstance
import no.ntnu.ihb.fmi4j.modeldescription.CoSimulationModelDescription
import no.ntnu.ihb.fmi4j.modeldescription.RealArray
import no.ntnu.ihb.fmi4j.modeldescription.StringArray
import no.ntnu.ihb.fmi4j.modeldescription.ValueReferences
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TFramedTransport
import org.apache.thrift.transport.TSocket
import java.io.File
import java.io.FileOutputStream
import java.net.ServerSocket
import java.nio.file.Files
import kotlin.concurrent.thread

class ProxySlave(
    private val remoteInfo: RemoteInfo,
    override val instanceName: String,
    override val modelDescription: CoSimulationModelDescription
) : SlaveInstance {

    private val client: FmuService.Client
    private val protocol: TBinaryProtocol

    override var isTerminated: Boolean = false
    override val lastStatus: FmiStatus
        get() = FmiStatus.OK
    override var simulationTime: Double = 0.0

    private var thread: Thread? = null

    init {

        val host = remoteInfo.host
        val port = if (remoteInfo.isLoopback && remoteInfo.hasPort) {
            remoteInfo.port!!
        } else {
            ServerSocket(0).localPort.also { availablePort ->

                thread = thread(true) {
                    val proxyFileName = "proxy_server.exe"
                    val proxy = ProxySlave::class.java.getResourceAsStream(proxyFileName)
                    val tmp = Files.createTempDirectory("vico_").toFile().apply {
                        mkdir()
                    }
                    val proxyFile = File(tmp, proxyFileName)
                    proxy.copyTo(FileOutputStream(proxyFile))
                    Runtime.getRuntime().exec(
                        arrayOf(
                            "proxyFileName",
                            "--port",
                            "$availablePort"
                        )
                    )
                    proxyFile.deleteOnExit()
                    tmp.deleteOnExit()
                }

            }
        }

        val transport = TFramedTransport.Factory().getTransport(TSocket(host, port))
        protocol = TBinaryProtocol(transport)
        transport.open()
        client = FmuService.Client(protocol)
    }

    override fun close() {
        if (protocol.transport.isOpen) {
            protocol.transport.close()
        }
        thread?.join()
    }

    override fun deSerializeFMUstate(state: ByteArray): FmuState {
        TODO("Not yet implemented")
    }

    override fun enterInitializationMode(): Boolean {
        val status = client.enterInitializationMode()
        return status == Status.OK_STATUS
    }

    override fun exitInitializationMode(): Boolean {
        val status = client.exitInitializationMode()
        return status == Status.OK_STATUS
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

    override fun setupExperiment(start: Double, stop: Double, tolerance: Double): Boolean {
        simulationTime = start
        val status = client.setupExperiment(start, stop, tolerance)
        return status == Status.OK_STATUS
    }

    override fun terminate(): Boolean {
        val status = client.terminate()
        isTerminated = true
        return status == Status.OK_STATUS
    }

    override fun doStep(stepSize: Double): Boolean {
        val status = client.step(simulationTime, stepSize)
        simulationTime += stepSize
        return status == Status.OK_STATUS
    }

    override fun readAll(
        intVr: ValueReferences?,
        intRefs: IntArray?,
        realVr: ValueReferences?,
        realRefs: DoubleArray?,
        boolVr: ValueReferences?,
        boolRefs: BooleanArray?,
        strVr: ValueReferences?,
        strRefs: StringArray?
    ): FmiStatus {
        TODO("Not yet implemented")
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

    override fun writeAll(
        intVr: ValueReferences?,
        intValues: IntArray?,
        realVr: ValueReferences?,
        realValues: DoubleArray?,
        boolVr: ValueReferences?,
        boolValues: BooleanArray?,
        strVr: ValueReferences?,
        strValues: StringArray?
    ): FmiStatus {
        TODO("Not yet implemented")
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
}
