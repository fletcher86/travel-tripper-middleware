package com.its.openpath.module.opscommon.event.persistence.notifreport.xml.error

import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jmx.export.annotation.ManagedResource
import org.springframework.stereotype.Service

import com.its.openpath.module.opscommon.event.persistence.AbstractEventPersistenceHandler;
import com.its.openpath.module.opscommon.model.messaging.ops.OpsMessage
import com.its.openpath.module.opscommon.util.OpsStatuses
import com.its.openpath.module.opscommon.util.PersistenceMessageBusQueueNames
import com.its.openpath.module.opscommon.util.TimeUUIDUtils

@Service("NotifReportResponseErrorsEventXMLPersistenceHandler")
@ManagedResource('OPENPATH:name=/module/opscommon/event/persistence/NotifReportResponseErrorsEventXMLPersistenceHandler')
class NotifReportResponseErrorsEventXMLPersistenceHandler extends AbstractEventPersistenceHandler
{
  private static Logger sLogger = LoggerFactory.getLogger(NotifReportResponseErrorsEventXMLPersistenceHandler.getName())
  
  /**
   * Constructor
   */
  NotifReportResponseErrorsEventXMLPersistenceHandler()
  {
    sLogger.info "Instantiated ..."
  }
  
  /**
   * Called by the Spring Framework after all Spring-manages beans are instantiated and wired up.
   */
  @PostConstruct
  void init( )
  {
    super.init()
    super.mOpsMessageBus.consumeFromQueue( PersistenceMessageBusQueueNames.RESERVATIONS_NOTIFICATION_REPORT_XML_ERRORS_RESPONSE, this, false )
  }
  
  /* (non-Javadoc)
   * @see com.its.openpath.module.opscommon.comm.bus.IMessageBusSubscriber#onMessage(java.lang.String, com.its.openpath.module.opscommon.model.messaging.ops.OpsMessage)
   */
  @Override
  public void onMessage( String topic, OpsMessage message )
  throws RuntimeException
  {
    UUID uuid = TimeUUIDUtils.toUUID ( message.getCorrelationIdBytes ().toByteArray () )
    String payload = message.getData ()
    this.persistPayload ( uuid, "notifreportresponseerrorsxml", payload?payload.bytes: "".getBytes () )
    this.updateTransactionStatus ( uuid, OpsStatuses.CONFIRMED )
  }
  
  
  @Override
  public String getSubscriberId()
  {
    return this.class.name;
  }

}
