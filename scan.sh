#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

WORKROOT=$(pwd)
cd ${WORKROOT}
TARGET_VERSION=$1

# Will be empty string on not installed
installed_version=$(trivy --version)

if [[ "$installed_version" != '' && "$installed_version" == *"$TARGET_VERSION"* ]]; then
  echo "Already installed; skipping"
  exit 0
fi

curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/master/contrib/install.sh | sudo sh -s -- -b /usr/local/bin

# unzip go environment
go_env="go1.17.6.linux-amd64.tar.gz"
wget -c https://go.dev/dl/go1.17.6.linux-amd64.tar.gz
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
#cd bolt-cli
# if [ $? -ne 0 ];
# then
#     echo "fail to go build"
#     exit 1
# fi
# echo "OK for go build"

#make build


../bolt scan-trivy --release tkg-1.4.0.yaml