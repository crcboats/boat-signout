// Copyright (c) 2011 Tim Niblett All Rights Reserved.
//
// File:        GaeUser.java  (26-Oct-2011)
// Author:      tim

//
// Copyright in the whole and every part of this source file belongs to
// Tim Niblett (the Author) and may not be used,
// sold, licenced, transferred, copied or reproduced in whole or in
// part in any manner or form or in or on any media to any person
// other than in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


package com.cilogi.shiro.gae;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.lang.util.SimpleByteSource;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class GaeUser implements Serializable {
    static final Logger LOG = Logger.getLogger(GaeUser.class.getName());

    static final int HASH_ITERATIONS = 1;
    static final String HASH_ALGORITHM = Sha256Hash.ALGORITHM_NAME;
    public static final String ROLE_MEMBER = "MEMBER";


    @Id
    private String name;
    
    private String displayName;
    
    private String pictureUrl;
    
    private String passwordHash;
    
    private String bio;
    
    private String contactInfo;

    /** The salt, used to make sure that a dictionary attack is harder given a list of all the
     *  hashed passwords, as each salt will be different.
     */
    private byte[] salt;

    private Set<String> roles;

    private Set<String> permissions;

    @Index
    private Date dateRegistered;

    private boolean isSuspended;

    /** For objectify to create instances on retrieval */
    private GaeUser() {
        this.roles = new HashSet<String>();
        this.permissions = new HashSet<String>();
    }

    GaeUser(String name) {
        this(name, null, new HashSet<String>(), new HashSet<String>());
    }

    
    GaeUser(String name, String password) {
        this(name, password, new HashSet<String>(), new HashSet<String>());
    }

    public GaeUser(String name, String displayName, Set<String> roles, Set<String> permissions) {
        this(name, displayName, null, roles, permissions);
    }
    
    public GaeUser(String name, String displayName, String password, Set<String> roles, Set<String> permissions) {
        this(name, displayName, password, roles, permissions, false);
    }

    GaeUser(String name, String displyName, String password, Set<String> roles, Set<String> permissions, boolean isRegistered) {
        Preconditions.checkNotNull(name, "User name (email) can't be null");
        Preconditions.checkNotNull(roles, "User roles can't be null");
        Preconditions.checkNotNull(permissions, "User permissions can't be null");
        this.name = name;
        this.displayName = displyName;

        this.salt = salt().getBytes();
        this.passwordHash = hash(password, salt);
        this.roles = roles;
        this.permissions = Collections.unmodifiableSet(permissions);
        this.dateRegistered = isRegistered ? new Date() : null;
        this.isSuspended = false;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setPassword(String password) {
        Preconditions.checkNotNull(password);
        this.salt = salt().getBytes();
        this.passwordHash = hash(password, salt);
    }

    public Date getDateRegistered() {
        return dateRegistered == null ? null : new Date(dateRegistered.getTime());
    }

    public boolean isRegistered() {
        return getDateRegistered() != null;
    }

    public void register() {
        dateRegistered = new Date();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
		return displayName;
	}

	public String getPasswordHash() {
        return passwordHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
    
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    private static ByteSource salt() {
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        return rng.nextBytes();
    }
    
    

    private static String hash(String password, byte[] salt) {
        return (password == null) ? null : new Sha256Hash(password, new SimpleByteSource(salt), HASH_ITERATIONS).toHex();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GaeUser) {
            GaeUser u = (GaeUser)o;
            return Objects.equal(getName(), u.getName()) &&
                   Objects.equal(getPasswordHash(), u.getPasswordHash());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, passwordHash);
    }
    
    
}
