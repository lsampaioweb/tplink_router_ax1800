package tplink.router.ax1800.model;

import java.util.Objects;

public class RoutingRule {

	private String target;
	private String netmask;
	private String gateway;
	private String interfaceName;
	private String description;

	public RoutingRule() {
	}

	public RoutingRule(String target, String netmask, String gateway, String interfaceName, String description) {
		this.target = target;
		this.netmask = netmask;
		this.gateway = gateway;
		this.interfaceName = interfaceName;
		this.description = description;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(netmask, target);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoutingRule other = (RoutingRule) obj;
		return Objects.equals(target, other.target);
	}

	@Override
	public String toString() {
		return "RoutingRule [target=" + target + ", netmask=" + netmask + ", gateway=" + gateway + ", interface="
				+ interfaceName + ", description=" + description + "]";
	}

}
