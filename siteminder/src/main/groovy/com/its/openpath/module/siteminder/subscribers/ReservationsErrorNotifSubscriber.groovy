package com.its.openpath.module.siteminder.subscribers


import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jmx.export.annotation.ManagedResource
import org.springframework.stereotype.Service

import com.its.openpath.module.opscommon.model.messaging.ops.OpsMessage
import com.its.openpath.module.opscommon.model.messaging.ops.OpsTxnType
import com.its.openpath.module.opscommon.util.EventMessageBusQueueNames
import com.its.openpath.module.siteminder.builders.ReservationsConfirmedErrorRequestXMLBuilder

/**
 * <code>ReservationsErrorNotifSubscriber</code>
 * <p/>
 * Consume Rate Update Notifications from the OPS Message Bus and POST them to the Siteminder IDS Web Service Endpoint.
 * The incoming Notification messages from the OPS bus are in the OPS JSON format, and the Notifications POSTed to
 * Siteminder are OTA XML encoded.
 * <p />
 * @author Kent Fletcher
 * @since May 2012
 */

@Service("ConfirmedReservationsErrorNotifSubscriber")
@ManagedResource('OPENPATH:name=/module/siteminder/handlers/ConfirmedReservationsErrorNotifSubscriber')
class ReservationsErrorNotifSubscriber extends AbstractSiteminderBaseSubscriber
{
  private static final Logger sLogger = LoggerFactory.getLogger( ReservationsErrorNotifSubscriber.class.name )
  
  @Autowired(required = true)
  protected ReservationsConfirmedErrorRequestXMLBuilder mSoapXMLbuilder

  ReservationsErrorNotifSubscriber()
  {
    super()
    sLogger.info "Instantiated ..."
  }
  
  @PostConstruct
  public void init()
  {
    mOpsMessageBus.consumeFromQueue( EventMessageBusQueueNames.RESERVATIONS_CONFIRMATION_ERROR_NOTIF_REQUEST, this, false )
  }
  
  @Override
  public String getSubscriberId()
  {
    return this.class.name
  }
  
  @Override
  public void onMessage( String topic, OpsMessage opsMessage )
  throws RuntimeException
  {
    OpsTxnType type = OpsTxnType.RESERVATION_NOTIFICATION_REPORT_ERRORS_MGMT
    super.processMessage(opsMessage, type)
  }
}
