#!groovy

pipeline {
    agent any

    parameters {
        string(description: '[OPTIONAL] trivy version', name: 'TRIVY_VERSION', defaultValue: "0.23.0")
    }

    options {
        //skipDefaultCheckout true
        buildDiscarder(logRotator(numToKeepStr: '30'))
        timeout(time: 360, unit: 'MINUTES')
    }

    stages {
        stage('Checkout external proj') {
              steps {
                 git branch: 'master',
                 credentialsId: 'github',
                 url: 'https://gitlab.eng.vmware.com/TKG/bolt/bolt-release-yamls.git'

            sh "ls -lat"
        }
    }
        stage('Scan') {
            steps {
                script {

                    checkout scm
                    // dir('atulabhi/temp/') {
                    //      withCredentials([file(credentialsId: 'github', variable: 'github'),file(credentialsId: 'github', variable: 'pass')]) {
                    //         git url: "https://${github}:${pass}@gitlab.eng.vmware.com/TKG/bolt/bolt-release-yamls.git"

                    //         sh "pwd"
                    //         sh "ls -a"
                    //     }
                        sh "./scan.sh"
                        sh "ls -a"
                    }
                }
            }
        }
    }
//}

