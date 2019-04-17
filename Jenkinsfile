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
            git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
            sh 'ls -la'
          }
        }
      }
    }
    stage('Clien') {
      steps {
        sh 'cd client'
        sh 'npm install'
        sh 'npm run build'
      }
    }
  }
}