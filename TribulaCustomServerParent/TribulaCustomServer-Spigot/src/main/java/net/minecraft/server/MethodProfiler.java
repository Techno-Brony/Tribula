package net.minecraft.server;

import java.util.List;

// CraftBukkit start - Strip down to empty methods, performance cost
public class MethodProfiler {

    public boolean a;
    public MethodProfiler() {}

    @SuppressWarnings("EmptyMethod")
    public void a() {
    }

    @SuppressWarnings("EmptyMethod")
    public void a(@SuppressWarnings("UnusedParameters") String s) {
    }

    @SuppressWarnings("EmptyMethod")
    public void b() {
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    public List<MethodProfiler.ProfilerInfo> b(String s) {
        return null;
    }

    @SuppressWarnings("EmptyMethod")
    public void c(@SuppressWarnings("UnusedParameters") String s) {
    }

    @SuppressWarnings("SameReturnValue")
    public String c() {
        return "";
    }

    public static final class ProfilerInfo implements Comparable<MethodProfiler.ProfilerInfo> {

        @SuppressWarnings("CanBeFinal")
        public double a;
        @SuppressWarnings({"unused", "CanBeFinal"})
        public double b;
        @SuppressWarnings("CanBeFinal")
        public String c;

        @SuppressWarnings("unused")
        public ProfilerInfo(String s, double d0, double d1) {
            this.c = s;
            this.a = d0;
            this.b = d1;
        }

        public int a(MethodProfiler.ProfilerInfo methodprofiler_profilerinfo) {
            return methodprofiler_profilerinfo.a < this.a ? -1 : (methodprofiler_profilerinfo.a > this.a ? 1 : methodprofiler_profilerinfo.c.compareTo(this.c));
        }

        public int compareTo(MethodProfiler.ProfilerInfo object) { // CraftBukkit: decompile error
            return this.a(object);
        }
    }
}
