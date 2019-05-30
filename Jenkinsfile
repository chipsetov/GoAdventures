pipeline {
  agent none
  stages {
    stage('test') {
      agent any
      steps {
        script {
          browser = sh(returnStdout: true, script: 'ssh 10.156.0.9 -oStrictHostKeyChecking=no diff /etc/bind/db.goadventures.com /etc/bind/db.goadventures.com-blue')
        }

        echo "${browser}"
        echo "${browser}"
        echo "${browser}"
        echo "${browser}"
        echo "${browser}"
        script {
          if (browser != '') {
            echo "more then zero"
          }
        }

        script {
          if (browser == '') {
            echo "zero"
          }
        }

      }
    }
    stage('A') {
      agent {
        docker {
          image 'ubuntu'
        }

      }
      steps {
        script {
          def ret = sh(script: 'dig +short www.softserveinc.com', returnStdout: true)
          println ret
        }

        echo "whoami".execute().text
        writeFile(file: 'props.txt', text: 'foo=bar')
        script {
          def props = readProperties file:'props.txt';
          env['foo'] = props['foo']
        }

      }
    }
    stage('B') {
      agent {
        label 'master'
      }
      steps {
        echo env.foo
      }
    }
    stage('ddddd') {
      agent any
      steps {
        script {
          echo "Testing the browser"
          echo "Testing the browser"
          File file = new File("out.txt")
          if (file.text == "First line") {echo "ssssssssssssssss"}
          println file.text
          "ls -l".execute().text
        }

      }
    }
    stage('build & SonarQube analysis') {
      agent {
        docker {
          image 'maven:3.6-jdk-11'
          args '--network=jenkinsnet -v /var/run/docker.sock:/var/run/docker.sock'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures && mvn clean package -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        sh 'cd server/goadventures && mvn clean dependency:go-offline'
        sh 'cd server/goadventures && mvn clean install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn sonar:sonar'
        }

      }
    }
    stage('make client image and push to dockerhub') {
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
      stage('make API image and push to dockerhub') {
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
            sh "ssh 35.246.200.246 docker run $registryapi:$BUILD_NUMBER"
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
          echo 'I succeeeded!'
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