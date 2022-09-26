package tplink.router.ax1800.model;

import java.util.Objects;

public class RoutingRule {

	String target;
	String netmask;
	String gateway;
	String interfaceName;
	String description;

	public RoutingRule(String target, String netmask, String gateway, String interfaceName, String description) {
		super();
		this.target = target;
		this.netmask = netmask;
		this.gateway = gateway;
		this.interfaceName = interfaceName;
		this.description = description;
	}

	public String getTarget() {
		return target;
	}

	public String getNetmask() {
		return netmask;
	}

	public String getGateway() {
		return gateway;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getDescription() {
		return description;
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
