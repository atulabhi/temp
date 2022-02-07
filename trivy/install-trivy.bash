#!/usr/bin/env bash

set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

TARGET_VERSION=$1

# Will be empty string on not installed
installed_version=$(trivy --version)

if [[ "$installed_version" != '' && "$installed_version" == *"$TARGET_VERSION"* ]]; then
  echo "Already installed; skipping"
  exit 0
fi

curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/master/contrib/install.sh | sudo sh -s -- -b /usr/local/bin