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
    }

    post {
        always {
            cleanWs()
        }
    }
}