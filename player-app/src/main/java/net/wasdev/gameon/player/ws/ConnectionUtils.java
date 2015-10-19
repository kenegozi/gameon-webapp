/*******************************************************************************
 * Copyright (c) 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package net.wasdev.gameon.player.ws;

import java.io.Closeable;
import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

/**
 * @author elh
 *
 */
public class ConnectionUtils {

	/**
	 * Simple text based broadcast.
	 *
	 * @param session
	 * @param id
	 * @param message
	 */
	public static void broadcast(Session session, String message) {
		for (Session s : session.getOpenSessions()) {
			sendText(s, message);
		}
	}

	/**
	 * @param session
	 * @param payload
	 */
	public static void sendText(Session session, String payload) {
		if (session.isOpen()) {
			try {
				session.getBasicRemote().sendText(payload);
			} catch (IOException e) {
				ConnectionUtils.tryToClose(session, new CloseReason(CloseCodes.UNEXPECTED_CONDITION, e.toString()));
			}
		}
	}

	public static Session getNextOpenSession(Session session) {
		for (Session s : session.getOpenSessions()) {
			if ( s.isOpen() && s != session ) // real is the same instance check
				return s;
		}
		return null;
	}

	/**
	 * Try to close the WebSocket session and give a reason for doing so.
	 *
	 * @param s
	 *            Session to close
	 * @param reason
	 *            {@link CloseReason} the WebSocket is closing.
	 */
	public static final void tryToClose(Session s, CloseReason reason) {
		try {
			s.close(reason);
		} catch (IOException e) {
			tryToClose(s);
		}
	}

	/**
	 * Try to close a session (usually once an error has already occurred).
	 *
	 * @param c
	 */
	public static final void tryToClose(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e1) {
			}
		}
	}
}