def call(Map c = [:]) {

    def severity = c.severity ?: 'high'
    def failOn = c.failOn?.toString()?.toBoolean()
    def snykOrg = c.snykOrg
    def projectName = c.projectName

    if (!c.snykToken) {
        error "snykToken(credentialsId)은 필수입니다"
    }

    withCredentials([
        string(credentialsId: c.snykToken, variable: 'TOKEN')
    ]) {
        sh """
            snyk auth \$TOKEN
            snyk test \
              --severity-threshold=${severity} \
              --org=${snykOrg} \
              --project-name=${projectName} \
              ${failOn == false ? '|| true' : ''}

            snyk-to-html -i \
            /var/jenkins_home/workspace/shared_lib_test/2026-01-19_snyk_report.json

            snyk monitor \
              --severity-threshold=${severity} \
              --org=${snykOrg} \
              --project-name=${projectName} \
              ${failOn == false ? '|| true' : ''}
        """
    }
}