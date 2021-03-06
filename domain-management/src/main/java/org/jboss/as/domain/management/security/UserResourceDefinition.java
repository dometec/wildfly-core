/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.domain.management.security;

import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.ObjectTypeAttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.ControllerResolver;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.security.CredentialReference;
import org.jboss.dmr.ModelType;

/**
 * {@link org.jboss.as.controller.ResourceDefinition} for an individual user resource in the management security realm's
 * domain-configuration-user-registry-based authentication mechanism.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class UserResourceDefinition extends SimpleResourceDefinition {

    public static final SimpleAttributeDefinition PASSWORD = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.PASSWORD, ModelType.STRING, false)
            .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, false, true))
            .setAllowExpression(true)
            .setAlternatives(CredentialReference.CREDENTIAL_REFERENCE)
            .build();

    public static final ObjectTypeAttributeDefinition CREDENTIAL_REFERENCE = CredentialReference.getAttributeBuilder(true, false)
            .setAlternatives(org.jboss.as.domain.management.ModelDescriptionConstants.VALUE)
            .build();

    public UserResourceDefinition() {
        super(PathElement.pathElement(ModelDescriptionConstants.USER),
                ControllerResolver.getDeprecatedResolver(SecurityRealmResourceDefinition.DEPRECATED_PARENT_CATEGORY,
                        "core.management.security-realm.authentication.xml.user"),
                UserAddHandler.INSTANCE,
                UserRemoveHandler.INSTANCE,
                OperationEntry.Flag.RESTART_NONE,
                OperationEntry.Flag.RESTART_NONE);
        setDeprecated(ModelVersion.create(1, 7));
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        resourceRegistration.registerReadWriteAttribute(PASSWORD, null, new UserWriteAttributeHandler());
        resourceRegistration.registerReadWriteAttribute(CREDENTIAL_REFERENCE, null, new UserWriteAttributeHandler());
    }
}
