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

            sh"""
            rm -rf ../yaml
            mkdir -p ../yaml 
            cp -r ./ ../yaml
            """
            checkout([
                    $class: 'GitSCM', branches: [[name: "main"]],
                    doGenerateSubmoduleConfigurations: false,extensions: [[$class:'CheckoutOption',timeout:60],[$class:'CloneOption',depth:0,noTags:true,reference:'',shallow:true,timeout:60]], submoduleCfg: [],
                    userRemoteConfigs: [[credentialsId: 'githubid', url: "https://gitlab.eng.vmware.com/TKG/bolt/bolt-cli.git"]]
                ])
             sh"""
            rm -rf ../bolt-cli
            mkdir -p ../bolt-cli 
            cp -r ./ ../bolt-cli
            """   
        }
    }
        stage('Scan') {
            steps {
                script {

                    checkout scm
                        sh "cp -r ../bolt-cli ."
                        sh "cp -r ../yaml ."
                        sh "./scan.sh ${params.TRIVY_VERSION}"
                       cleanWs deleteDirs: true
                    }
                }
            }
        }
    }
//}

