pipeline {
  agent {
    docker {
      image 'node:8.10.0'
    }

  }
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          steps {
            sh 'npm install'
          }
        }
        stage('check') {
          steps {
            sh 'ls -la'
            git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
          }
        }
      }
    }
  }
}