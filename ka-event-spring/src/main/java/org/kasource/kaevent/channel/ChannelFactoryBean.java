/**
 * 
 */
package org.kasource.kaevent.channel;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Rikard Wigforss
 *
 */
public class ChannelFactoryBean implements FactoryBean{

    @Resource
    private ChannelFactory channelFactory;
   
    private String name;


    @Override
    public Object getObject() throws Exception {
        
        return channelFactory.createChannel(name);
    }

    @Override
    public Class<?> getObjectType() {
        
        return Channel.class;
    }

    

    @Override
    public boolean isSingleton() {
        return false;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

   
}
