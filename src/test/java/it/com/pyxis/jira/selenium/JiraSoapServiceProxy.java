package it.com.pyxis.jira.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;

import com.atlassian.jira.rpc.soap.client.JiraSoapServiceService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteException;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira_soapclient.SOAPSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class JiraSoapServiceProxy {

	private JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
	private SOAPSession soapSession;
	private JiraSoapService jiraSoapService = null;
//	private String token = null;
	
	private String baseUrl = "http://localhost:2990/jira/rpc/soap/jirasoapservice-v2";

	public JiraSoapServiceProxy() {
		getJiraSoapService();
	}

	public String getToken() {
		return soapSession.getAuthenticationToken();
	}

	public JiraSoapService getJiraSoapService() {
		if (jiraSoapService == null) {
			try {
				soapSession = new SOAPSession(new URL(baseUrl));
				jiraSoapService = soapSession.getJiraSoapService();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} finally {
				// TODO: handle exception
			}
			assertNotNull(jiraSoapService);
		}
		return jiraSoapService;
	}

	public JiraSoapServiceProxy login(String userName, String password) {
		try {
			soapSession.connect(userName, password);
//			token = getJiraSoapService().login(userName, password);
		} catch (RemoteAuthenticationException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.rmi.RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(getToken());
		
		return this;
	}

	public void deleteIssue(String issueKey) {
		try {
			getJiraSoapService().deleteIssue(getToken(), issueKey);
		} catch (RemotePermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.rmi.RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RemoteIssue getIssue(String issueKey) {
		try {
			return getJiraSoapService().getIssue(getToken(), issueKey);
		} catch (RemotePermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.rmi.RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
