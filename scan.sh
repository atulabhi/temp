#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

WORKROOT=$(pwd)
cd ${WORKROOT}

# unzip go environment
go_env="go1.6.2.linux-amd64.tar.gz"
wget -c http://path/to/go/go1.6.2.linux-amd64.tar.gz
tar -zxf $go_env
if [ $? -ne 0 ];
then
    echo "fail in extract go"
    exit 1
fi
echo "OK for extract go"
rm -rf $go_env

# prepare PATH, GOROOT and GOPATH
export PATH=$(pwd)/go/bin:$PATH
export GOROOT=$(pwd)/go
export GOPATH=$(pwd)

# build
cd path/to/your/project
go build
if [ $? -ne 0 ];
then
    echo "fail to go build"
    exit 1
fi
echo "OK for go build"

cd bolt-cli

make build