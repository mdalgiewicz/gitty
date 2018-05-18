package pl.dalgim.gitty.github.infrastructure.required.gitclient.rest.repository;

import pl.dalgim.gitty.github.query.RepositoryInfo;
import pl.dalgim.gitty.github.query.RepositorySearchParams;

/**
 * @author Mateusz Dalgiewicz
 */
class RepositoryMapper {

  static RepositoryInfo map(RepositoryInfoResponse response) {
    if (response == null) {
      return null;
    }
    return new RepositoryInfo(
        response.getFullName(),
        response.getDescription(),
        response.getCloneUrl(),
        response.getStars(),
        response.getCreatedAt()
    );
  }

  static RepositoryInfoRequest mapToRequest(RepositorySearchParams params) {
    if (params == null) {
      return null;
    }
    return new RepositoryInfoRequest(
        params.getOwner(),
        params.getRepositoryName()
    );
  }
}
