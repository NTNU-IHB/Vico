
function createPlane(shape, mat) {
    return new THREE.Mesh(new THREE.BoxGeometry(shape.width, shape.height), mat)
}

function createBox(shape, mat) {
    return new THREE.Mesh(new THREE.BoxGeometry(shape.width, shape.height, shape.depth), mat)
}

function createSphere(shape, mat) {
    return new THREE.Mesh(new THREE.SphereGeometry(shape.radius, 32, 32), mat)
}

function createCylinder(shape, mat) {
    return new THREE.Mesh(new THREE.CylinderGeometry(shape.radius, shape.radius, shape.height, 32, 32), mat)
}

function createCapsule(shape, mat) {
    return new Capsule(shape.radius, shape.height, mat, 32, 32)
}

function createTrimesh(shape, mat) {
    const geom = new THREE.BufferGeometry()
    const vertices = new Float32Array(shape.vertices)
    const normals = new Float32Array(shape.normals)
    const uvs = new Float32Array(shape.uvs)
    geom.setIndex(shape.indices)
    geom.setAttribute("position", new THREE.BufferAttribute(vertices, 3))
    geom.setAttribute("normal", new THREE.BufferAttribute(normals, 3))
    geom.setAttribute("uvs", new THREE.BufferAttribute(uvs, 2))
    geom.computeBoundingSphere()
    return new THREE.Mesh(geom, mat)
}

function loadObj(shape, callback) {

    const source = shape.source.replace(/\\/g, "/")
    const objLoader = new THREE.OBJLoader()

    let httpGet = function (url, callback) {
        let xmlHttp = new XMLHttpRequest();
        xmlHttp.onreadystatechange = function () {
            if (xmlHttp.readyState === 4) {
                if (xmlHttp.status === 200) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
        }
        xmlHttp.open("GET", url, true); // true for asynchronous
        xmlHttp.send(null);
    }

    let load = function (hasMtl) {
        objLoader.load("/assets?" + source, function (obj) {
            callback(obj)
        })
    }

    let split = source.split("/")
    let mtlUrl = split.pop().replace(".obj", ".mtl")
    let mtlUrlPath = "/assets?" + split.join("/") + "/"
    httpGet(mtlUrlPath + mtlUrl, function (success) {
        if (success) {
            const mtlLoader = new THREE.MTLLoader()
            mtlLoader.crossOrigin = '';
            mtlLoader.setPath(mtlUrlPath)
            mtlLoader.load(mtlUrl, function (materials) {
                materials.preload()
                objLoader.setMaterials(materials)
                load(true)
            })
        } else {
            load(false)
        }
    })
}

function createTrimeshFromSource(shape, callback) {
    const ext = shape.source.split(".").pop()
    switch (ext) {
        case "obj":
            loadObj(shape, callback)
            break
    }
}

function createMesh(shape, mat, callback) {
    switch (shape.type) {
        case "plane":
            callback(createPlane(shape.data, mat))
            break
        case "box":
            callback(createBox(shape.data, mat))
            break
        case "sphere":
            callback(createSphere(shape.data, mat))
            break
        case "cylinder":
            callback(createCylinder(shape.data, mat))
            break
        case "capsule":
            callback(createCapsule(shape.data, mat))
            break
        case "trimesh":
            callback(createTrimesh(shape.data, mat))
            break
        case "trimeshWithSource":
            createTrimeshFromSource(shape.data, callback)
            break
    }
}
