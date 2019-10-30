/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.leg.alrr.cursos.util;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * @author fosa
 */
public class Ldap {

    static String ldapbasedn = "dc=al,dc=rr,dc=leg,dc=br";
    static String ldapurl = "ldap://10.95.16.4:389/";

    public static boolean ldapAuth(String user, String password) {
        if (user.length() == 0) {
            return false;
        }
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapurl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        String uid = user;

        DirContext ctx = null;

        try {
            ctx = new InitialDirContext(env);

            // Step 2: Search the directory
            String base = ldapbasedn;
            String filter = "(uid={0})";
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[0]);
            ctls.setReturningObjFlag(true);
            @SuppressWarnings("rawtypes")
            NamingEnumeration enm = ctx.search(base, filter, new String[]{uid}, ctls);

            String dn = null;

            if (enm.hasMore()) {
                SearchResult result = (SearchResult) enm.next();
                dn = result.getNameInNamespace();

                System.out.println("dn: " + dn);
            }

            if (dn == null || enm.hasMore()) {
                // uid not found or not unique
                throw new NamingException("Authentication failed");
            }

            // Step 3: Bind with found DN and given password
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            // Perform a lookup in order to force a bind operation with JNDI
            ctx.lookup(dn);
            System.out.println("Authentication successful");

            enm.close();

            return true;

        } catch (NamingException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ctx.close();
            } catch (NamingException ex) {
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
