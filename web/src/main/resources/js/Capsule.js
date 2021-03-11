Capsule = function (radius, cylinderHeight, mat, radiusSegments, heightSegments) {
    let capsule = new THREE.Object3D();
    radiusSegments = radiusSegments || 16;
    heightSegments = heightSegments || 8;
    let body = new THREE.Mesh(new THREE.CylinderGeometry(radius, radius, cylinderHeight, radiusSegments, heightSegments, true), mat),
        upperSphere = new THREE.Mesh(new THREE.SphereGeometry(radius, radiusSegments, heightSegments, 0, Math.PI * 2, 0, Math.PI / 2), mat),
        lowerSphere = new THREE.Mesh(new THREE.SphereGeometry(radius, radiusSegments, heightSegments, 0, Math.PI * 2, Math.PI / 2, Math.PI / 2), mat);
    upperSphere.position.set(0, cylinderHeight / 2, 0);
    lowerSphere.position.set(0, -cylinderHeight / 2, 0);
    capsule.add(body);
    capsule.add(upperSphere);
    capsule.add(lowerSphere);
    return capsule;
};
