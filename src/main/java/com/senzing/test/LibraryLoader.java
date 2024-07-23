package com.senzing.test;

import com.sun.jna.Library;

public interface LibraryLoader extends Library {
    public long LoadLibraryExA(String   lpLibFileName,
                               long     hFile,
                               int      dwFlags);
}
