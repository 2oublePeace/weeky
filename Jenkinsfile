pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    git 'https://github.com/2oublePeace/weeky.git'
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    sh './gradlew build test'
                }
            }
        }

        stage('Dockerize and Push') {
            steps {
                script {
                    sh 'docker build -t 2oublepeace/weeky-persistence:latest ./persistence'
                    sh 'docker build -t 2oublepeace/weeky-webapp:latest ./webapp'
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                        sh 'docker push 2oublepeace/weeky-persistence:latest'
                        sh 'docker push 2oublepeace/weeky-webapp:latest'
                    }
                }
            }
        }

        stage('Trigger deploy hook') {
            steps {
                script {
                    sh 'wget -O /dev/null https://api.render.com/deploy/srv-coq7lu5jm4es73agqot0?key=5F1fq_mPFP0'
                    sh 'wget -O /dev/null https://api.render.com/deploy/srv-coq711cf7o1s73ec4ag0?key=vLB-nrCyYq0'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}