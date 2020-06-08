def var
node {
  try{
    stage("Preparation") {
      git(
        url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
        branch: BRANCH_NAME
      )
    }
    stage("Create jar"){
        sh 'mvn clean install -B -DskipTests'
    }
    stage("Test") {
        sh 'mvn test'
   }
   stage("Quality"){
       sh "mvn sonar:sonar  -Dsonar.branch=${BRANCH_NAME}"

   }
   stage("Save jar"){
       archiveArtifacts "target/*.jar"
   }
   stage('Publish') {
     nexusPublisher nexusInstanceId: 'localNexus', nexusRepositoryId: 'mvn-releases',
     packages: [[$class: 'MavenPackage', mavenAssetList:
     [[classifier: '', extension: '', filePath: 'target/practica-jenkins-0.0.1-SNAPSHOT.jar']],
     mavenCoordinate: [artifactId: 'practica-jenkins', groupId: 'es.codeurjc.ci', packaging: 'jar', version: '1.0.0']]]
      }
  }
   finally {
      junit "target/*-reports/TEST-*.xml"
    }
}
