pipeline {
  agent {
    docker {
      image 'node:8.10.0'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'npm install'
      }
    }
  }
}