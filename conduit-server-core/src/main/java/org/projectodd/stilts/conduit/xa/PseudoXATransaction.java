/*
 * Copyright 2011 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projectodd.stilts.conduit.xa;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.logging.Logger;
import org.projectodd.stilts.conduit.spi.MessageConduit;
import org.projectodd.stilts.stomp.Acknowledger;
import org.projectodd.stilts.stomp.StompMessage;

class PseudoXATransaction {

    private static Logger log = Logger.getLogger(PseudoXATransaction.class);

    PseudoXATransaction() {
    }

    void setRollbackOnly(boolean rollbackOnly) {
        this.rollbackOnly = rollbackOnly;
    }

    boolean isRollbackOnly() {
        return this.rollbackOnly;
    }

    boolean isReadOnly() {
        return (this.sentMessages.isEmpty() && this.acks.isEmpty() && this.nacks.isEmpty() );
    }

    void addSentMessage(StompMessage message) {
        this.sentMessages.add( message );
    }

    boolean hasSentMessages() {
        return ! this.sentMessages.isEmpty();
    }

    void addAck(Acknowledger acknowledger) {
        this.acks.add(  acknowledger );
    }

    boolean hasAcks() {
        return ! this.acks.isEmpty();
    }

    void addNack(Acknowledger acknowledger) {
        this.nacks.add(acknowledger);
    }

    boolean hasNacks() {
        return ! this.nacks.isEmpty();
    }

    public void commit(MessageConduit conduit) {
        for (StompMessage each : sentMessages ) {
            try {
                conduit.send( each );
            } catch (Exception e) {
                log.errorf(e, "Cannot send message: %s", each);
            }
        }

        sentMessages.clear();

        for ( Acknowledger each : this.acks ) {
            try {
                each.ack();
            } catch (Exception e) {
                log.errorf(e, "Cannot ack: %s", each);
            }
        }

        this.acks.clear();

        for ( Acknowledger each : this.nacks ) {
            try {
                each.nack();
            } catch (Exception e) {
                log.errorf(e, "Cannot nack: %s", each);
            }
        }

        this.nacks.clear();
    }

    public void rollback(MessageConduit messageConduit) {
        sentMessages.clear();
        acks.clear();
    }

    private boolean rollbackOnly;

    private ConcurrentLinkedQueue<StompMessage> sentMessages = new ConcurrentLinkedQueue<StompMessage>();
    private ConcurrentLinkedQueue<Acknowledger> acks = new ConcurrentLinkedQueue<Acknowledger>();
    private ConcurrentLinkedQueue<Acknowledger> nacks = new ConcurrentLinkedQueue<Acknowledger>();

}
