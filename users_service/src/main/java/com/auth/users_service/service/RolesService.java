package com.auth.users_service.service;

@Service
@lombok.RequiredArgsConstructor
public class RolesService {

    public CreateRoleResponse createRole(CreateRoleRequest request) {
        // Here you would add logic to save the role to a database, etc.
        return new CreateRoleResponse(request.getName());
    }

    public ReadRoleResponse readRole(ReadRoleRequest request) {
        // Here you would add logic to save the role to a database, etc.
        return new ReadRoleResponse(request.getName());
    }

    public List<ReadRoleResponse> readAllRoles() {
        // Here you would add logic to save the role to a database, etc.
        return List.of(new ReadRoleResponse("Example Role"));
    }

    public DeleteRoleResponse deleteRole(DeleteRoleRequest request) {
        // Here you would add logic to save the role to a database, etc.
        return new DeleteRoleResponse(request.getName());
    }

    public EditRoleResponse editRole(EditRoleRequest request) {
        // Here you would add logic to save the role to a database, etc.
        return new EditRoleResponse(request.getName());
    }

}
