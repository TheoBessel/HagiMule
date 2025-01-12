package file.fragment;

import device.ClientInfo;

public class FileFragment {
    private String name;
    private Integer size;
    private Integer offset;
    private ClientInfo owner;

    public FileFragment(String name, Integer size, Integer offset, ClientInfo owner) {
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
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }
    public Integer getOffset() {
        return offset;
    }
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
    public ClientInfo getOwner() {
        return owner;
    }
    public void setOwner(ClientInfo owner) {
        this.owner = owner;
    }
}
