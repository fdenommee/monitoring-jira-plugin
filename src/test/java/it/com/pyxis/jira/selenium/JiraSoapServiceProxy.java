package it.com.pyxis.jira.selenium;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;

import com.atlassian.jira.rpc.soap.client.JiraSoapServiceService;
import com.atlassian.jira.rpc.soap.client.JiraSoapServiceServiceLocator;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemoteException;
import com.atlassian.jira.rpc.soap.client.RemoteIssue;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

public class JiraSoapServiceProxy {

	private JiraSoapServiceService jiraSoapServiceGetter = new JiraSoapServiceServiceLocator();
	private JiraSoapService jiraSoapService = null;
	private String token = null;

	public JiraSoapServiceProxy() {
		getJiraSoapService();
	}

	public String getToken() {
		return token;
	}

	public JiraSoapService getJiraSoapService() {
		if (jiraSoapService == null) {
			try {
				jiraSoapService = jiraSoapServiceGetter.getJirasoapserviceV2();
			} catch (javax.xml.rpc.ServiceException e) {
				e.printStackTrace();
			}
			assertNotNull(jiraSoapService);
		}
		return jiraSoapService;
	}

	public JiraSoapServiceProxy login(String user, String pwd) {
		try {
			token = getJiraSoapService().login(user, pwd);
		} catch (RemoteAuthenticationException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.rmi.RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(token);
		
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
