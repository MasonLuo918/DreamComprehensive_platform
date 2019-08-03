package com.Dream.util.parse.docparse;

import com.Dream.entity.ActivityProve;

import java.util.List;

public interface DocParser {
    List<ActivityProve> getResult(int proveType);
}
