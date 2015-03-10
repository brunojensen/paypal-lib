package com.paypal.api.payments;

import java.util.HashMap;
import java.util.List;

import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.HttpMethod;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;
import com.paypal.base.rest.RESTUtil;
import com.paypal.base.sdk.info.SDKVersionImpl;

public class Event  extends PayPalResource {

	/**
	 * Identifier of the Webhooks event resource.
	 */
	private String id;

	/**
	 * Time the resource was created.
	 */
	private String createTime;

	/**
	 * Name of the resource contained in resource element.
	 */
	private String resourceType;

	/**
	 * Name of the event type that occurred on resource, identified by data_resource element, to trigger the Webhooks event.
	 */
	private String eventType;

	/**
	 * A summary description of the event. E.g. A successful payment authorization was created for $$
	 */
	private String summary;

	/**
	 * This contains the resource that is identified by resource_type element.
	 */
	private Object resource;

	/**
	 * Hateoas links.
	 */
	private List<Links> links;

	/**
	 * Default Constructor
	 */
	public Event() {
	}


	/**
	 * Setter for id
	 */
	public Event setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Getter for id
	 */
	public String getId() {
		return this.id;
	}


	/**
	 * Setter for createTime
	 */
	public Event setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	/**
	 * Getter for createTime
	 */
	public String getCreateTime() {
		return this.createTime;
	}


	/**
	 * Setter for resourceType
	 */
	public Event setResourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}

	/**
	 * Getter for resourceType
	 */
	public String getResourceType() {
		return this.resourceType;
	}


	/**
	 * Setter for eventType
	 */
	public Event setEventType(String eventType) {
		this.eventType = eventType;
		return this;
	}

	/**
	 * Getter for eventType
	 */
	public String getEventType() {
		return this.eventType;
	}


	/**
	 * Setter for summary
	 */
	public Event setSummary(String summary) {
		this.summary = summary;
		return this;
	}

	/**
	 * Getter for summary
	 */
	public String getSummary() {
		return this.summary;
	}


	/**
	 * Setter for resource
	 */
	public Event setResource(Object resource) {
		this.resource = resource;
		return this;
	}

	/**
	 * Getter for resource
	 */
	public Object getResource() {
		return this.resource;
	}


	/**
	 * Setter for links
	 */
	public Event setLinks(List<Links> links) {
		this.links = links;
		return this;
	}

	/**
	 * Getter for links
	 */
	public List<Links> getLinks() {
		return this.links;
	}


	/**
	 * Retrieves the Webhooks event resource identified by event_id. Can be used to retrieve the payload for an event.
	 * @param accessToken
	 *            Access Token used for the API call.
	 * @param eventId
	 *            String
	 * @return Event
	 * @throws PayPalRESTException
	 */
	public static Event get(String accessToken, String eventId) throws PayPalRESTException {
		APIContext apiContext = new APIContext(accessToken);
		return get(apiContext, eventId);
	}

	/**
	 * Retrieves the Webhooks event resource identified by event_id. Can be used to retrieve the payload for an event.
	 * @param apiContext
	 *            {@link APIContext} used for the API call.
	 * @param eventId
	 *            String
	 * @return Event
	 * @throws PayPalRESTException
	 */
	public static Event get(APIContext apiContext, String eventId) throws PayPalRESTException {
		if (apiContext == null) {
			throw new IllegalArgumentException("APIContext cannot be null");
		}
		if (apiContext.getAccessToken() == null || apiContext.getAccessToken().trim().length() <= 0) {
			throw new IllegalArgumentException("AccessToken cannot be null or empty");
		}
		if (apiContext.getHTTPHeaders() == null) {
			apiContext.setHTTPHeaders(new HashMap<String, String>());
		}
		apiContext.getHTTPHeaders().put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
		apiContext.setSdkVersion(new SDKVersionImpl());
		if (eventId == null) {
			throw new IllegalArgumentException("eventId cannot be null");
		}
		Object[] parameters = new Object[] {eventId};
		String pattern = "v1/notifications/webhooks-events/{0}";
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = "";
		return configureAndExecute(apiContext, HttpMethod.GET, resourcePath, payLoad, Event.class);
	}


	/**
	 * Resends the Webhooks event resource identified by event_id.
	 * @param accessToken
	 *            Access Token used for the API call.
	 * @return Event
	 * @throws PayPalRESTException
	 */
	public Event resend(String accessToken) throws PayPalRESTException {
		APIContext apiContext = new APIContext(accessToken);
		return resend(apiContext);
	}

	/**
	 * Resends the Webhooks event resource identified by event_id.
	 * @param apiContext
	 *            {@link APIContext} used for the API call.
	 * @return Event
	 * @throws PayPalRESTException
	 */
	public Event resend(APIContext apiContext) throws PayPalRESTException {
		if (apiContext == null) {
			throw new IllegalArgumentException("APIContext cannot be null");
		}
		if (apiContext.getAccessToken() == null || apiContext.getAccessToken().trim().length() <= 0) {
			throw new IllegalArgumentException("AccessToken cannot be null or empty");
		}
		if (apiContext.getHTTPHeaders() == null) {
			apiContext.setHTTPHeaders(new HashMap<String, String>());
		}
		apiContext.getHTTPHeaders().put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
		apiContext.setSdkVersion(new SDKVersionImpl());
		if (this.getId() == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Object[] parameters = new Object[] {this.getId()};
		String pattern = "v1/notifications/webhooks-events/{0}/resend";
		String resourcePath = RESTUtil.formatURIPath(pattern, parameters);
		String payLoad = "";
		return configureAndExecute(apiContext, HttpMethod.POST, resourcePath, payLoad, Event.class);
	}


	/**
	 * Retrieves the list of Webhooks events resources for the application associated with token. The developers can use it to see list of past webhooks events.
	 * @param accessToken
	 *            Access Token used for the API call.
	 * @return EventList
	 * @throws PayPalRESTException
	 */
	public static EventList list(String accessToken, String queryParams) throws PayPalRESTException {
		APIContext apiContext = new APIContext(accessToken);
		return list(apiContext, queryParams);
	}

	/**
	 * Retrieves the list of Webhooks events resources for the application associated with token. The developers can use it to see list of past webhooks events.
	 * @param apiContext
	 *            {@link APIContext} used for the API call.
	 * @return EventList
	 * @throws PayPalRESTException
	 */
	public static EventList list(APIContext apiContext, String queryParams) throws PayPalRESTException {
		if (apiContext == null) {
			throw new IllegalArgumentException("APIContext cannot be null");
		}
		if (apiContext.getAccessToken() == null || apiContext.getAccessToken().trim().length() <= 0) {
			throw new IllegalArgumentException("AccessToken cannot be null or empty");
		}
		if (apiContext.getHTTPHeaders() == null) {
			apiContext.setHTTPHeaders(new HashMap<String, String>());
		}
		apiContext.getHTTPHeaders().put(Constants.HTTP_CONTENT_TYPE_HEADER, Constants.HTTP_CONTENT_TYPE_JSON);
		apiContext.setSdkVersion(new SDKVersionImpl());
		String resourcePath = "v1/notifications/webhooks-events" + queryParams;
		String payLoad = "";
		return configureAndExecute(apiContext, HttpMethod.GET, resourcePath, payLoad, EventList.class);
	}


}
