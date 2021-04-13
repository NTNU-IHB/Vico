
conan remote add osp https://osp.jfrog.io/artifactory/api/conan/conan-local -f
conan install . -s build_type=Release --install-folder=build/cmake-build-release --build=missing
