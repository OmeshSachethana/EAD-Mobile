package com.example.ecommerce.utils

import com.auth0.android.jwt.JWT
import com.example.ecommerce.data.model.User

object JwtUtils {

    fun decodeToken(token: String): User? {
        val jwt = JWT(token)

        val name = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name").asString() ?: return null
        val email = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress").asString() ?: return null
        val role = jwt.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role").asString() ?: return null

        return User(name, email, role)
    }
}