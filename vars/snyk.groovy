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
        """
    }
}