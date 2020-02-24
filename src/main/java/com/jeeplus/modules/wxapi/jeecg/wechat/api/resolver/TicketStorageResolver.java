package com.jeeplus.modules.wxapi.jeecg.wechat.api.resolver;

import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.Ticket;
import com.jeeplus.modules.wxapi.jeecg.wechat.api.entity.TicketStore;

public abstract class TicketStorageResolver {

    private TicketStore ticketStore;

    public TicketStorageResolver(){
        this.ticketStore = new TicketStore();
    }

    public TicketStorageResolver(TicketStore store){
        this.ticketStore = store;
    }

    public abstract Ticket getTicket(String type);

    public abstract void saveTicket(String type, Ticket ticket);

    public TicketStorageResolver setTicketStore(TicketStore store){
        this.ticketStore = store;
        return this;
    }

    public TicketStore getTicketStore(){
        return this.ticketStore;
    }

}
