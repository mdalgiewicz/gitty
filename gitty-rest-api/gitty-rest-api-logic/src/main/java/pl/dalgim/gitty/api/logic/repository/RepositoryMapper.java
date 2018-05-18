package pl.dalgim.gitty.api.logic.repository;

import pl.dalgim.gitty.api.repository.RepositoryInfoDto;
import pl.dalgim.gitty.github.query.RepositoryInfo;

/**
 * @author Mateusz Dalgiewicz
 */
class RepositoryMapper {

  static RepositoryInfoDto map(RepositoryInfo repositoryInfo) {
    if (repositoryInfo == null) {
      return null;
    }
    return new RepositoryInfoDto(
        repositoryInfo.getFullName(),
        repositoryInfo.getDescription(),
        repositoryInfo.getCloneUrl(),
        repositoryInfo.getStars(),
        repositoryInfo.getCreatedAt()
    );
  }
}
