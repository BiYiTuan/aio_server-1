/**
 * Copyright (C) 2003  Manfred Andres
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Created on 28.09.2003
 */

package freecs.commands;
import freecs.interfaces.ICommand;
import freecs.content.MessageState;

/**
 * @author Manfred Andres
 *
 * freecs.commands
 */
public class CmdReplyMessage extends AbstractCommand {
	private final String cmd= "/r";
	private final String version = "1.0";
	private static final ICommand selve=new CmdReplyMessage();

	private CmdReplyMessage () { }
	
	public static ICommand getInstance () {
		return selve;
	}
	   
    public Object instanceForSystem() {
        return this;
    }
    
    public String getCmd() {
        return cmd;
    }

	/**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
	
	public boolean execute (MessageState msgState, String param) {
		msgState.usercontext = msgState.sender.getForeignPrivateUser();
		if (msgState.usercontext == null) 
			return false;
		if (msgState.usercontext.isLoggedOut ()) {
			msgState.param = msgState.usercontext.getName() ;
			msgState.msgTemplate = "error.user.notOnServer.singular";
			msgState.sender.sendMessage (msgState.mp);
			msgState.sender.setPrivateUser(null);
			return false;
		}
		if (param.length () < 1) {
			msgState.msgTemplate = "error.m.noMessage";
		   	msgState.sender.sendMessage (msgState.mp);
			return false;
		}
		if (cantHearYou(msgState, true))
            return false;
		msgState.message=param;
		sendPrivateMessage (msgState, msgState.usercontext, param);
        messageLog(msgState, msgState.usercontext,"PrivatMessage");
		return true;
	}
}
