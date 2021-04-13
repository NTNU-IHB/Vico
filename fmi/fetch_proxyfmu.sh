#!/usr/bin/env bash

conan remote add osp https://osp.jfrog.io/artifactory/api/conan/conan-local -f

unameOut="$(uname -s)"
case "${unameOut}" in
    MINGW*)     conan install . -s build_type=Release --install-folder=build/cmake-build-release --build=missing;;
    Linux*)     conan install . -s build_type=Release -s compiler.libcxx=libstdc++11 --install-folder=build/cmake-build-release --build=missing;;
esac
