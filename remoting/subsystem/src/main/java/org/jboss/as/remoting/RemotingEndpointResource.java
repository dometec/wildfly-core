/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.remoting;

import static org.jboss.as.remoting.RemotingSubsystemRootResource.IO_WORKER_CAPABILITY;
import static org.jboss.as.remoting.RemotingSubsystemRootResource.REMOTING_ENDPOINT_CAPABILITY;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.logging.Level;


import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PersistentResourceDefinition;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.remoting.logging.RemotingLogger;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.remoting3.RemotingOptions;
import org.wildfly.extension.io.OptionAttributeDefinition;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2013 Red Hat Inc.
 */
public class RemotingEndpointResource extends PersistentResourceDefinition {
    protected static final SimpleAttributeDefinition WORKER = new SimpleAttributeDefinitionBuilder(CommonAttributes.WORKER, ModelType.STRING)
            .setRequired(false)
            .setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES)
            .setValidator(new StringLengthValidator(1))
            .setDefaultValue(new ModelNode("default"))
            .setCapabilityReference(IO_WORKER_CAPABILITY, REMOTING_ENDPOINT_CAPABILITY)
            .build();


    public static final OptionAttributeDefinition SEND_BUFFER_SIZE = OptionAttributeDefinition.builder("send-buffer-size", RemotingOptions.SEND_BUFFER_SIZE).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_SEND_BUFFER_SIZE)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition RECEIVE_BUFFER_SIZE = OptionAttributeDefinition.builder("receive-buffer-size", RemotingOptions.RECEIVE_BUFFER_SIZE).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_RECEIVE_BUFFER_SIZE)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition BUFFER_REGION_SIZE = OptionAttributeDefinition.builder("buffer-region-size", RemotingOptions.BUFFER_REGION_SIZE).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition TRANSMIT_WINDOW_SIZE = OptionAttributeDefinition.builder("transmit-window-size", RemotingOptions.TRANSMIT_WINDOW_SIZE).setDefaultValue(new ModelNode(RemotingOptions.INCOMING_CHANNEL_DEFAULT_TRANSMIT_WINDOW_SIZE)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition RECEIVE_WINDOW_SIZE = OptionAttributeDefinition.builder("receive-window-size", RemotingOptions.RECEIVE_WINDOW_SIZE).setDefaultValue(new ModelNode(RemotingOptions.INCOMING_CHANNEL_DEFAULT_RECEIVE_WINDOW_SIZE)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition MAX_OUTBOUND_CHANNELS = OptionAttributeDefinition.builder("max-outbound-channels", RemotingOptions.MAX_OUTBOUND_CHANNELS).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_MAX_OUTBOUND_CHANNELS)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition MAX_INBOUND_CHANNELS = OptionAttributeDefinition.builder("max-inbound-channels", RemotingOptions.MAX_INBOUND_CHANNELS).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_MAX_INBOUND_CHANNELS)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition AUTHORIZE_ID = OptionAttributeDefinition.builder("authorize-id", RemotingOptions.AUTHORIZE_ID).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition AUTH_REALM = OptionAttributeDefinition.builder("auth-realm", RemotingOptions.AUTH_REALM).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition AUTHENTICATION_RETRIES = OptionAttributeDefinition.builder("authentication-retries", RemotingOptions.AUTHENTICATION_RETRIES).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_AUTHENTICATION_RETRIES)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition MAX_OUTBOUND_MESSAGES = OptionAttributeDefinition.builder("max-outbound-messages", RemotingOptions.MAX_OUTBOUND_MESSAGES).setDefaultValue(new ModelNode(RemotingOptions.OUTGOING_CHANNEL_DEFAULT_MAX_OUTBOUND_MESSAGES)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition MAX_INBOUND_MESSAGES = OptionAttributeDefinition.builder("max-inbound-messages", RemotingOptions.MAX_INBOUND_MESSAGES).setDefaultValue(new ModelNode(RemotingOptions.INCOMING_CHANNEL_DEFAULT_MAX_OUTBOUND_MESSAGES)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition HEARTBEAT_INTERVAL = OptionAttributeDefinition.builder("heartbeat-interval", RemotingOptions.HEARTBEAT_INTERVAL).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_HEARTBEAT_INTERVAL)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition MAX_INBOUND_MESSAGE_SIZE = OptionAttributeDefinition.builder("max-inbound-message-size", RemotingOptions.MAX_INBOUND_MESSAGE_SIZE).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_MAX_INBOUND_MESSAGE_SIZE)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition MAX_OUTBOUND_MESSAGE_SIZE = OptionAttributeDefinition.builder("max-outbound-message-size", RemotingOptions.MAX_OUTBOUND_MESSAGE_SIZE).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_MAX_OUTBOUND_MESSAGE_SIZE)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition SERVER_NAME = OptionAttributeDefinition.builder("server-name", RemotingOptions.SERVER_NAME).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();
    public static final OptionAttributeDefinition SASL_PROTOCOL = OptionAttributeDefinition.builder("sasl-protocol", RemotingOptions.SASL_PROTOCOL).setDefaultValue(new ModelNode(RemotingOptions.DEFAULT_SASL_PROTOCOL)).setAllowExpression(true).setFlags(AttributeAccess.Flag.RESTART_ALL_SERVICES).build();


    protected static final PathElement ENDPOINT_PATH = PathElement.pathElement("configuration", "endpoint");
    protected static final Collection<AttributeDefinition> ATTRIBUTES;

    static final RemotingEndpointResource INSTANCE = new RemotingEndpointResource();

    public static final java.util.List<OptionAttributeDefinition> OPTIONS = Arrays.asList(SEND_BUFFER_SIZE, RECEIVE_BUFFER_SIZE, BUFFER_REGION_SIZE, TRANSMIT_WINDOW_SIZE, RECEIVE_WINDOW_SIZE,
            MAX_OUTBOUND_CHANNELS, MAX_INBOUND_CHANNELS, AUTHORIZE_ID, AUTH_REALM, AUTHENTICATION_RETRIES, MAX_OUTBOUND_MESSAGES,
            MAX_INBOUND_MESSAGES, HEARTBEAT_INTERVAL, MAX_INBOUND_MESSAGE_SIZE, MAX_OUTBOUND_MESSAGE_SIZE, SERVER_NAME, SASL_PROTOCOL);

    static {
        ATTRIBUTES = new LinkedHashSet<AttributeDefinition>(Collections.singletonList(WORKER));
        ATTRIBUTES.addAll(OPTIONS);
    }

    private RemotingEndpointResource() {
        super(ENDPOINT_PATH, RemotingExtension.getResourceDescriptionResolver("endpoint"));
    }

    public Collection<AttributeDefinition> getAttributes() {
        //noinspection unchecked
        return ATTRIBUTES;
    }

    @Override
    public void registerOperations(ManagementResourceRegistration resourceRegistration) {
        super.registerOperations(resourceRegistration);
        super.registerAddOperation(resourceRegistration, new RemotingEndpointAdd(), OperationEntry.Flag.RESTART_NONE);
        super.registerRemoveOperation(resourceRegistration, ReloadRequiredRemoveStepHandler.INSTANCE, OperationEntry.Flag.RESTART_NONE);
    }
    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        //register as normal
        super.registerAttributes(resourceRegistration);
        //override
        resourceRegistration.unregisterAttribute(WORKER.getName());
        resourceRegistration.registerReadWriteAttribute(WORKER, null, new WorkerAttributeWriteHandler(WORKER));
    }

    class WorkerAttributeWriteHandler extends ReloadRequiredWriteAttributeHandler {

        public WorkerAttributeWriteHandler(AttributeDefinition... definitions) {
            super(definitions);
        }

        @Override
        protected void finishModelStage(OperationContext context, ModelNode operation, String attributeName, ModelNode newValue,
                ModelNode oldValue, Resource model) throws OperationFailedException {
            super.finishModelStage(context, operation, attributeName, newValue, oldValue, model);
            context.addResponseWarning(Level.WARNING, RemotingLogger.ROOT_LOGGER.warningOnWorkerChange(newValue.asString()));
        }
    }
}
