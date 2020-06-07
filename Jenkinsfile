// node {
//   stage("Init") {
//     sh 'echo hello'
//   }
//
//   stage("Test") {
//     sh 'echo bye'
//   }
//
//   stage("Security test") {
//     sh 'echo vulnerabilities'
//   }
// }
pipeline {
 tools {
   maven "M3"
 }
 agent any
 stages {
   stage("Preparation") {
     steps {
       git 'https://github.com/pjcalvo84/mastercloudapps-cicd.git'
     }
   }
   stage("Create jar") {
       steps {
           sh "mvn package -DskipTests"
       }
   }
   stage("Start app") {
    steps {
     sh "cd target;java -jar *.jar > out.log & echo \$! > pid"
    }
   }
   stage("Test") {
     steps {
       sh "mvn test -DskipITs"
     }
   }
 }
 post {
    always {
      junit "**/target/surefire-reports/TEST-*.xml"
      sh "kill \$(cat target/pid)"
      archive "target/out.log"
    }
    success {
      archive "target/*.jar"
    }
 }
}