package File.Fragment;

import Device.ClientInfo;

public class FileFragment {
    private String name;
    private Long size;
    private Long offset;
    private ClientInfo owner;

    public FileFragment(String name, Long size, Long offset, ClientInfo owner) {
        this.name = name;
        this.size = size;
        this.offset = offset;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public Long getOffset() {
        return offset;
    }
    public void setOffset(Long offset) {
        this.offset = offset;
    }
    public ClientInfo getOwner() {
        return owner;
    }
    public void setOwner(ClientInfo owner) {
        this.owner = owner;
    }
}
