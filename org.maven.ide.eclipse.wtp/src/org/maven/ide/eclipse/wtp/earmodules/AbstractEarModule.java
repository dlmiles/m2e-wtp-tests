/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.earmodules;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.artifact.Artifact;


/**
 * A base implementation of an {@link EarModule}.
 * 
 * @author <a href="snicoll@apache.org">Stephane Nicoll</a>
 */
public abstract class AbstractEarModule implements EarModule {

  protected static final String MODULE_ELEMENT = "module";

  protected static final String JAVA_MODULE = "java";

  protected static final String ALT_DD = "alt-dd";

  private String uri;

  private Artifact artifact;

  // Those are set by the configuration

  private String groupId;

  private String artifactId;

  private String classifier;

  protected String bundleDir;

  protected String bundleFileName;

  protected Boolean excluded = Boolean.FALSE;

  protected Boolean unpack = null;

  protected String altDeploymentDescriptor;

  /**
   * Empty constructor to be used when the module is built based on the configuration.
   */
  public AbstractEarModule() {
  }

  /**
   * Creates an ear module from the artifact.
   * 
   * @param a the artifact
   */
  public AbstractEarModule(Artifact a, String bundleFileName) {
    this.artifact = a;
    this.groupId = a.getGroupId();
    this.artifactId = a.getArtifactId();
    this.classifier = a.getClassifier();
    this.bundleDir = null;
    this.bundleFileName = bundleFileName;
  }

  public Artifact getArtifact() {
    return artifact;
  }

  public String getUri() {
    if(uri == null) {
      if(getBundleDir() == null) {
        uri = getBundleFileName();
      } else {
        uri = getBundleDir() + getBundleFileName();
      }
    }
    return uri;
  }

  /**
   * Returns the artifact's groupId.
   * 
   * @return the group Id
   */
  public String getGroupId() {
    return groupId;
  }

  /**
   * Returns the artifact's Id.
   * 
   * @return the artifact Id
   */
  public String getArtifactId() {
    return artifactId;
  }

  /**
   * Returns the artifact's classifier.
   * 
   * @return the artifact classifier
   */
  public String getClassifier() {
    return classifier;
  }

  /**
   * Returns the bundle directory. If null, the module is bundled in the root of the EAR.
   * 
   * @return the custom bundle directory
   */
  public String getBundleDir() {
    if(bundleDir != null) {
      bundleDir = cleanBundleDir(bundleDir);
    }
    return bundleDir;
  }

  /**
   * Returns the bundle file name. If null, the artifact's file name is returned.
   * 
   * @return the bundle file name
   */
  public String getBundleFileName() {
    return bundleFileName;
  }

  /**
   * The alt-dd element specifies an optional URI to the post-assembly version of the deployment descriptor file for a
   * particular Java EE module. The URI must specify the full pathname of the deployment descriptor file relative to the
   * application's root directory.
   * 
   * @return the alternative deployment descriptor for this module
   * @since JavaEE 5
   */
  public String getAltDeploymentDescriptor() {
    return altDeploymentDescriptor;
  }

  /**
   * Specify whether this module should be excluded or not.
   * 
   * @return true if this module should be skipped, false otherwise
   */
  public boolean isExcluded() {
    return excluded.booleanValue();
  }

  public Boolean shouldUnpack() {
    return unpack;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getType()).append(":").append(groupId).append(":").append(artifactId);
    if(classifier != null) {
      sb.append(":").append(classifier);
    }
    if(artifact != null) {
      sb.append(":").append(artifact.getVersion());
    }
    return sb.toString();
  }

  /**
   * Cleans the bundle directory so that it might be used properly.
   * 
   * @param bundleDir the bundle directory to clean
   * @return the cleaned bundle directory
   */
  static String cleanBundleDir(String bundleDir) {
    if(bundleDir == null) {
      return bundleDir;
    }

    // Using slashes
    bundleDir = bundleDir.replace('\\', '/');

    // Remove '/' prefix if any so that directory is a relative path
    if(bundleDir.startsWith("/")) {
      bundleDir = bundleDir.substring(1, bundleDir.length());
    }

    if(bundleDir.length() > 0 && !bundleDir.endsWith("/")) {
      // Adding '/' suffix to specify a directory structure if it is not empty
      bundleDir = bundleDir + "/";
    }

    return bundleDir;
  }

  /**
   * Specify if the objects are both null or both equal.
   * 
   * @param first the first object
   * @param second the second object
   * @return true if parameters are either both null or equal
   */
  static boolean areNullOrEqual(Object first, Object second) {
    if(first != null) {
      return first.equals(second);
    } else {
      return second == null;
    }
  }

}