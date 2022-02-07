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
        stage('Scan') {
            steps {
                script {

                    //checkout scm
                    //dir('atulabhi/temp/') {
                        // withCredentials([file(credentialsId: 'github', variable: 'github')]) {

                            sh "./scan.sh"
                        //}
                    //}
                }
            }
        }
    }
}

