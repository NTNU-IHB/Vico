#!/usr/bin/env bash

conan remote add osp https://osp.jfrog.io/artifactory/api/conan/conan-local -f
conan install . -s build_type=Release -s compiler.libcxx=libstdc++11 --install-folder=build/cmake-build-release --build=missing

