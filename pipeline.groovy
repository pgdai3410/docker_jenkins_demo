pipeline {
    agent any
    
    environment{
        GIT_REPOSITORY_URL = 'https://github.com/pgdai3410/docker_jenkins_demo'
        DOCKER_IMAGE_NAME = 'pgdai3410/docker_jenkins_demo'
        IMAGE_TAG = '1.0'
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    try{
                        git branch: 'main', url: GIT_REPOSITORY_URL
                        
                    } 
                    catch(Exception e){
                        echo "Failed to clone repository: ${e.message}"
                        error "Failed to clone repository"
                    }    
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script{
                    try{
                        docker.build(DOCKER_IMAGE_NAME)
                    }
                    catch(Exception e){
                        echo "Failed to build docker image: ${e.message}"
                        error "Failed to build docker image"
                    } 
                }
            
            }
        }
        stage('Push to DockerHub') {
            steps {
                script{
                    try{
                        withCredentials([usernamePassword(credentialsId: 'my-docker-hub-credentials-id',usernameVariable: 'pgdai3410', passwordVariable: 'dckr_pat_T3vK7ZNrWez8JcMZzqPLTEWWdyw')]){
                            docker.withRegistry('https://index.docker.io/v1/'){
                                docker.image(DOCKER_IMAGE_NAME).push(IMAGE_TAG)
                            }
                        }
                        
                    }
                    catch(Exception e){
                        echo "Failed to push docker image to registry: ${e.message}"
                        error "Failed to push docker image"
                    } 
                }
            }
        }
    }
}