#!groovy

pipeline {
    agent any

    parameters {
        string(description: '[OPTIONAL] trivy version', name: 'TRIVY_VERSION', defaultValue: "0.23.0")
    }

    options {
        skipDefaultCheckout true
        buildDiscarder(logRotator(numToKeepStr: '30'))
        timeout(time: 360, unit: 'MINUTES')
    }

    stages {
        stage('Checkout external prerequsite') {
              steps {
                checkout([
                    $class: 'GitSCM', branches: [[name: "main"]],
                    doGenerateSubmoduleConfigurations: false,extensions: [[$class:'CheckoutOption',timeout:60],[$class:'CloneOption',depth:0,noTags:true,reference:'',shallow:true,timeout:60]], submoduleCfg: [],
                    userRemoteConfigs: [[credentialsId: 'githubid', url: "https://gitlab.eng.vmware.com/TKG/bolt/bolt-release-yamls.git"]]
                ])

            sh "ls -lat"
            sh "pwd"
            sh"""
            rm -rf ../yaml
            mkdir -p ../yaml 
            cp -r ./ ../yaml
            ls -lat ../yaml
            """
        }
    }
        stage('Scan') {
            steps {
                script {

                    checkout scm
                        sh "./scan.sh"
                        sh "ls -a"
                        sh "cp -r ../yaml ."
                        sh "pwd"
                       sh "ls -a"
                       cleanWs deleteDirs: true
                    }
                }
            }
        }
    }
//}

