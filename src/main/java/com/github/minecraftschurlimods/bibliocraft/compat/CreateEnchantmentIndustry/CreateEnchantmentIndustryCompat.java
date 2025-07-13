package com.github.minecraftschurlimods.bibliocraft.compat.CreateEnchantmentIndustry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import plus.dragons.createenchantmentindustry.api.PrintEntryRegisterEvent;

public class CreateEnchantmentIndustryCompat {
    static public void  load(IEventBus bus)
    {
        bus.addListener(CreateEnchantmentIndustryCompat::registerEntry);
    }
    static public void registerEntry(PrintEntryRegisterEvent register)
    {
        register.register(new BigBookPrintEntry());
    }
}
