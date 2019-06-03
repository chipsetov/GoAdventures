pipeline {
  agent none
  stages {
    stage('DNS check') {
      agent {
        label 'master'
      }
      steps {
        script {
          browser = sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "diff /etc/bind/db.goadventures.com /etc/bind/db.goadventures.com-blue; true"')
        }

        script {
          echo browser
          boolean containsData = browser?.trim()

          if (browser == '') {
            echo "same"
            sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "rm /etc/bind/db.goadventures.com; true"')
            sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "cp /etc/bind/db.goadventures.com-green /etc/bind/db.goadventures.com; true"')
            sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "sudo /etc/init.d/bind9 restart; true"')
            env.DEPLOYSERVER = '10.156.0.8'
          }
          else {
            echo "not the same"
            sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "rm /etc/bind/db.goadventures.com; true"')
            sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "cp /etc/bind/db.goadventures.com-blue /etc/bind/db.goadventures.com; true"')
            sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no "sudo /etc/init.d/bind9 restart; true"')
            env.DEPLOYSERVER = '10.156.0.5'
          }
        }

        sh "ssh ${env.DEPLOYSERVER} -oStrictHostKeyChecking=no uname -a"
        echo env.DEPLOYSERVER
      }
    }
    stage('Build & SonarQube analysis') {
      agent {
        docker {
          image 'maven:3.6-jdk-11'
          args '--network=jenkinsnet -v /var/run/docker.sock:/var/run/docker.sock'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures/src/main/resources && sed -i \'s/db:5432/10.156.0.14:5432/g\' application.properties'
        sh 'cd server/goadventures && mvn clean package -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        sh 'cd server/goadventures && mvn clean dependency:go-offline'
        sh 'cd server/goadventures && mvn clean install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn sonar:sonar'
        }

      }
    }
    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate true
        }

      }
    }
    stage('Make client image and push to dockerhub') {
      agent any
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        script {
          dir ('client') {
            dockerImage = docker.build registry + ":$BUILD_NUMBER" }
          }

          script {
            docker.withRegistry( '', registryCredential ) {
              dockerImage.push()
            }
            sh "docker rmi $registry:$BUILD_NUMBER"
          }

        }
      }
      stage('Make API image and push to dockerhub') {
        agent any
        steps {
          git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
          script {
            dir ('server/goadventures') {
              dockerImage = docker.build registryapi + ":$BUILD_NUMBER" }
            }

            script {
              docker.withRegistry( '', registryCredential ) {
                dockerImage.push()
              }
              sh "docker rmi $registryapi:$BUILD_NUMBER"
            }

          }
        }
        stage('Deploy') {
          agent any
          steps {
            sh "ssh ${env.DEPLOYSERVER} /home/shakh_anton/dockerrm.sh"
            sh "ssh ${env.DEPLOYSERVER} docker run -p 8080:8080 -d $registryapi:$BUILD_NUMBER"
            sh "ssh ${env.DEPLOYSERVER} docker run -p 3000:3000 -d $registry:$BUILD_NUMBER"
          }
        }
      }
      environment {
        buildserver = 'blue'
        registry = 'sgsh/goad-app'
        registryapi = 'sgsh/goad-api'
        registryCredential = 'docker-hub'
        dockerfilemavenversion = '1'
      }
      post {
        success {
          echo 'I succeeded!'
          mail(subject: "${env.JOB_NAME}-${env.BUILD_NUMBER}", body: 'GoAdventures build', to: 'shakh.softgroup@gmail.com')
          slackSend(channel: '#jenkins', color: '#00FF00', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")

        }

        failure {
          echo 'I failed :('
          mail(subject: "${env.JOB_NAME}-${env.BUILD_NUMBER}", body: 'GoAdventures failed', to: 'shakh.softgroup@gmail.com')
          slackSend(channel: '#jenkins', color: '#FF0000', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")

        }

      }
    }