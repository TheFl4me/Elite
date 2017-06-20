package com.minecraft.plugin.elite.general.database;

import java.sql.Connection;

public interface DatabaseCore {

    Connection getConnection();

    void queue(BufferStatement bs);
}