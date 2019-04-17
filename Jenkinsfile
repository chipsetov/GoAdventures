pipeline {
  agent none
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          agent {
            docker {
              image 'node:8.10.0'
            }

          }
          steps {
            sh 'npm install'
          }
        }
        stage('check') {
          agent {
            docker {
              image 'node:8.10.0'
            }

          }
          steps {
            git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
            sh 'ls -la'
          }
        }
      }
    }
    stage('Clien') {
      agent {
        docker {
          image 'node:8.10.0'
        }

      }
      steps {
        sh 'cd client'
        sh 'cd client && npm install'
        sh 'cd client && npm run build'
      }
    }
  }
}