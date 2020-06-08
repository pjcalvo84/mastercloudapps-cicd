def var
node {
  try{
    stage("Preparation") {
    sh("printenv")
//       git(
          //         url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git'
          //       )

      def isPr() {
          env.CHANGE_ID != null
      }
      def extensions = []
      if (isPr()) {
          extensions = [[$class: 'PreBuildMerge', options: [mergeRemote: "refs/remotes/origin/pull-requests", mergeTarget: "${env.CHANGE_ID}/from"]]]
      }
      checkout([$class: 'GitSCM',
          doGenerateSubmoduleConfigurations: false,
          extensions: extensions,
          submoduleCfg: [],
          userRemoteConfigs: [[
              refspec: '+refs/heads/*:refs/remotes/origin/* +refs/pull-requests/*:refs/remotes/origin/pull-requests/*',
              url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git'
          ]]
      ])

      git status
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
