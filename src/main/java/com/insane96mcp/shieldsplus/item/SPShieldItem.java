package com.insane96mcp.shieldsplus.item;

import net.minecraft.world.item.ShieldItem;

public class SPShieldItem extends ShieldItem {
    private double blockedDamage;

    public SPShieldItem(double blockedDamage, Properties p_43089_) {
        super(p_43089_);
        this.blockedDamage = blockedDamage;
    }

    public double getBlockedDamage() {
        return this.blockedDamage;
    }
}
