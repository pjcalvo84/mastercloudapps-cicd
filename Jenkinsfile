def var
node {
  try{
    stage("Preparation") {
      git(
        url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git'
      )
    }
    stage("Test") {

        sh 'mvn verify -Dspring.datasource.url=jdbc:mysql://${var}/blog -Dspring.datasource.username=mark -Dspring.datasource.password=hello22'

   }
   stage("Quality"){
        sh("printenv")
       //sh 'mvn sonar:sonar sonar.pullrequest.branch=feature/pruebaPR'
   }
   stage("Save jar"){
       archive "target/*.jar"
   }
  }
   finally {
      sh "docker stop \$(docker ps -aq -f 'name=db')"
      sh "docker rm \$(docker ps -aq -f 'name=db')"
      junit "target/*-reports/TEST-*.xml"
      archive "target/out.log"
    }
}